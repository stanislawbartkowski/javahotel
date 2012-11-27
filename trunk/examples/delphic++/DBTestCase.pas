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
    procedure Testconnect;
    procedure Test13;
  private
    function get13: integer;
    function createList13: TList<row13>;
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
      Q := DBTest.getQ('SELECT RID() AS R FROM TEST13 WHERE NUMB = 13');
  end;
  L := TList<row13>.Create;
  with Q do
  begin
    ExecSQL;
    Open;
    repeat
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

procedure TestDBTest.Test13;
var
  P: TSQLStoredProc;
  num13: integer;
  L: TList<row13>;
begin
  DBTest.connect;
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
  CheckNotEquals(0, num13);
  L := createList13;
  CheckEquals(num13, L.Count);
  changetoPerfect(L);
  num13 := get13;
  CheckEquals(0, num13);

  DBTest.disconnect;
end;

initialization

// Register any test cases with the test runner
RegisterTest(TestDBTest.Suite);

end.
