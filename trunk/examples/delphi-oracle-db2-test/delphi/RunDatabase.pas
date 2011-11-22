unit RunDatabase;

interface

uses
  System.SysUtils, DB, SQLExpr, DBXDynalink,
  Windows, Messages, Variants, Classes, DBXDb2, FMTBcd,
  DBClient, Provider, DBXOdbc, DbxOracle;

type
  { database type }
  DatabaseType = (DB2, Oracle);

type
  DBTest = class
  public
    { Constructor : creates database object specific to database given }
    Constructor Create(paramT: DatabaseType);


    { Connect to to database according to database type }
    procedure connect;

    { Disconnect }
    procedure disconnect;

    { Run TRUNCATE_TABLE for a table given.}
    procedure truncateTable(table: string);

    { Adds message string to MESSAGES table.
      Execute A_LOG.ADD_MESSAGE stored procedure. }
    procedure addMessageToPackageLog(mess: String);

    { Get number of rows in MESSAGES table.
      Executes A_LOG.GET_NUMB stored prcoedure. }
    function getNumberOfMessage: integer;

    { Constructs TSQLQuery class connect to database. }
    function getQ(statement: String): TSQLQuery;

    { Get TSQLStoredProc class connected to database. }
    function getSP(packName: String; procName: String): TSQLStoredProc;
      Overload;
    function getSP(procName: String): TSQLStoredProc; Overload;

    { Get field type related to BCD for storing stored prcoedure NUMBER parameter }
    function getBCDType : TFieldType;

    { Get database type }
    function getT : DatabaseType;

  private
  var
    Conn: TSQLConnection;
    T: DatabaseType;
    procedure ConnectOracle;
    procedure ConnectDB2;
    procedure ConnectODBC;
  end;

implementation

constructor DBTest.Create(paramT: DatabaseType);
begin
  T := paramT;
  Conn := TSQLConnection.Create(nil);
end;

procedure DBTest.ConnectOracle;
begin
  With Conn do
  begin
    Conn.DriverName := 'Oracle';
    Params.Values['USER_NAME'] := 'testuser';
    Params.Values['PASSWORD'] := 'testuser';
    Params.Values['DATABASE'] := 'TEST';
  end;
end;

procedure DBTest.ConnectDB2;
begin
  With Conn do
  begin
    DriverName := 'DB2';
    VendorLib := 'db2cli.dll';
    LibraryName := 'dbxdb2.dll';
    GetDriverFunc := 'getSQLDriverDB2';
    Params.Values['USER_NAME'] := 'db2inst1';
    Params.Values['PASSWORD'] := 'db2inst1';
    Params.Values['DATABASE'] := 'tsample';
  end;
end;

procedure DBTest.ConnectODBC;
begin
  With Conn do
  begin
    DriverName := 'Odbc';
    {
      VendorLib := 'db2cli.dll';
      LibraryName := 'dbxdb2.dll';
      GetDriverFunc := 'getSQLDriverDB2';
    }
    Params.Values['USER_NAME'] := 'db2inst1';
    Params.Values['PASSWORD'] := 'db2inst1';
    Params.Values['DATABASE'] := 'tsample';
  end;
end;

procedure DBTest.connect;
begin
  case T of
    DB2:
      ConnectDB2;
    Oracle:
      ConnectOracle;
  end;
  Conn.Open;
end;

{
dFecha:TDateTime
ParamByName('dfecha').AsSQLTimeStamp := DateTimeToSQLTimeStamp
}

procedure DBTest.disconnect;
begin
  Conn.Close;
end;

function DBTest.getBCDType : TFieldType;
begin
  case T of
  Oracle : result := ftBCD;
  DB2: result := ftFloat;
  end;
end;

procedure DBTest.truncateTable(table: string);
var
  Q: TSQLQuery;
begin
  Q := getQ('TRUNCATE TABLE ' + table);
  Q.ExecSQL();
end;

procedure DBTest.addMessageToPackageLog(mess: String);
var
  SP: TSQLStoredProc;
begin
  SP := getSP('A_TESTLOG', 'ADD_MESSAGE');
  With SP do
  begin
    Params.CreateParam(TFieldType.ftString, 'MESS', TParamType.ptInput);
    ParamByName('MESS').AsString := mess;
    ExecProc;
  end;

end;

function DBTest.getNumberOfMessage: integer;
var
  SP: TSQLStoredProc;
begin
  SP := getSP('A_TESTLOG', 'GET_NUM');
  With SP do
  begin
{    Params.CreateParam(TFieldType.ftBCD, 'NUM', TParamType.ptOutput); }
{    Params.CreateParam(TFieldType.ftFloat, 'NUM', TParamType.ptOutput); }
    Params.CreateParam(getBCDType, 'NUM', TParamType.ptOutput);
    ExecProc;
    result := ParamByName('NUM').AsInteger;
  end;
end;

function DBTest.getSP(packName: String; procName: String): TSQLStoredProc;
var
  SP: TSQLStoredProc;
begin
  SP := TSQLStoredProc.Create(Nil);
  with SP do
  begin
    SQLConnection := Conn;
    SchemaName := '';
    PackageName := packName;
    StoredProcName := procName;
    case T of
      Oracle:
        ParamCheck := true;
      DB2:
        ParamCheck := false;
    end;
    Params.Clear;
  end;
  result := SP;
end;

function DBTest.getSP(procName: String): TSQLStoredProc;
begin
  result := getSP('', procName);
end;

function DBTest.getQ(statement: String): TSQLQuery;
var
  Q: TSQLQuery;
begin

  Q := TSQLQuery.Create(nil);
  with Q do
  begin
    SQLConnection := Conn;
    Q.SQL.Add(statement);
  end;
  result := Q;
end;

function DBTest.getT : DatabaseType;
begin
  result := T;
end;

end.
