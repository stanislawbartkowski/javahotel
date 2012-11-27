unit DBTestCase;

interface

uses
  TestFramework,
  System.SysUtils,
  Data.DB,
  Data.SqlExpr,
  Data.DBXCommon,
  DBAccess,
  Generics.Collections;

type
  { Keep rowid, but one can use UnicodeStrings directly. }
  row13 = record
    rowid: UnicodeString;
    constructor Create(r: UnicodeString);
  end;

type
  TestDBTest = class(TTestCase)
  strict private
    DBTest: DBConnect;
  public
    procedure SetUp; override;
    procedure TearDown; override;
  published
    { Two test cases available, the first is only for connection. }
    procedure Testconnect;
    procedure Test13;
  private
    { Returns number of 13s in the table. }
    function get13: integer;
    { Create a list of rowid with 13 in the table. }
    function createList13: TList<row13>;
    { Change all 13 to 6 according to list of rowids. }
    procedure changetoPerfect(L: TList<row13>);
  end;

implementation

constructor row13.Create(r: UnicodeString);
begin
  rowid := r;
end;

procedure TestDBTest.Testconnect;
begin
  DBTest.connect;
  DBTest.disconnect;
end;

procedure TestDBTest.SetUp;
begin
  { DBTest := DBConnect.Create(Oracle); }
  DBTest := DBConnect.Create(DB2);
end;

procedure TestDBTest.TearDown;
begin
  DBTest.Free;
  DBTest := nil;
end;

procedure TestDBTest.changetoPerfect(L: TList<row13>);
var
  r: row13;
  Q: TSQLQuery;
  i: integer;
begin
  { rowid for oracle and rid() for db2. }
  case DBTest.getT of
    Oracle:
      Q := DBTest.getQ('UPDATE TEST13 SET NUMB = 6 WHERE ROWID = :ROWID');
    DB2:
      Q := DBTest.getQ('UPDATE TEST13 SET NUMB = 6 WHERE RID() = :ROWID');
  end;
  for i := 0 to L.Count - 1 do
  begin
    r := L[i];
    with Q do
    begin
      { although rid() is a number can be passed as a string. }
      Params.ParamByName('ROWID').AsString := r.rowid;
      prepared := true;
      ExecSQL;
    end;
  end;
end;

function TestDBTest.createList13;
var
  Q: TSQLQuery;
  L: TList<row13>;
  rowid: UnicodeString;
  r: row13;
begin
  { Oracle does not allow ROWID as column name alias, use R }
  case DBTest.getT of
    Oracle:
      Q := DBTest.getQ('SELECT ROWID AS R FROM TEST13 WHERE NUMB = 13');
    DB2:
      { rid() for DB2. Although rid() is BIGINT can be passed as a string. }
      Q := DBTest.getQ('SELECT RID() AS R FROM TEST13 WHERE NUMB = 13');
  end;
  L := TList<row13>.Create;
  with Q do
  begin
    ExecSQL;
    Open;
    repeat
      { although rid() is a number can be read as a string. }
      rowid := FieldByName('R').AsString;
      r := row13.Create(rowid);
      L.Add(r);
      Next;
    until Eof;
  end;
  Q.Free;
  result := L;
end;

function TestDBTest.get13;
var
  Q: TSQLQuery;
begin
  Q := DBTest.getQ('SELECT COUNT(*) AS NUM FROM TEST13 WHERE NUMB = 13');
  with Q do
  begin
    ExecSQL;
    Open;
    result := FieldByName('NUM').AsInteger;
    Close;
  end;
  Q.Free;
end;

{ Test scenario:
  1. Populate the table by calling POPULATETABLE procedure
  2. Check that there are some 13s in the table\
  3. Collect rowids with 13s
  4. Replace 13 with 6
  5. Check that number of rows with 13s is 0 now
}
procedure TestDBTest.Test13;
var
  P: TSQLStoredProc;
  num13: integer;
  L: TList<row13>;
begin
  DBTest.connect;
  { Step 1 }
  P := DBTest.getSP('POPULATETABLE');
  with P do
  begin
    Params.CreateParam(TFieldType.ftInteger, 'N_OF_ROWS', TParamType.ptInput);
    ParamByName('N_OF_ROWS').AsInteger := 2000;
    ExecProc;
    Close
  end;
  P.Free;
  num13 := get13;
  { Step 2 }
  CheckNotEquals(0, num13);
  { Step 3 }
  L := createList13;
  CheckEquals(num13, L.Count);
  { Step 4 }
  changetoPerfect(L);
  num13 := get13;
  { Step 5 }
  CheckEquals(0, num13);

  DBTest.disconnect;
end;

initialization

// Register any test cases with the test runner
RegisterTest(TestDBTest.Suite);

end.
