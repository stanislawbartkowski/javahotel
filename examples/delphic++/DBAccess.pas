unit DBAccess;
{ Simple unit which encapsulates some database access methods }

interface

uses
  System.SysUtils, DB, SQLExpr, DBXDynalink,
  Windows, Messages, Variants, Classes, DBXDb2, FMTBcd,
  DBClient, Provider, DBXOdbc, DbxOracle;

type
  { database type }
  DatabaseType = (DB2, Oracle);

type
  DBConnect = class
  public
    { Constructor : creates database object specific to database given }
    Constructor Create(paramT: DatabaseType);
    { Connect to to database according to database type }
    procedure connect;

    { Disconnect }
    procedure disconnect;

    { Get TSQLStoredProc class connected to database. }
    function getSP(packName: String; procName: String): TSQLStoredProc;
      Overload;

    { Get TSQLStoredProc assuming default schema. }
    function getSP(procName: String): TSQLStoredProc; Overload;

    { Constructs TSQLQuery class connect to database. }
    function getQ(statement: String): TSQLQuery;

    { Get current database type. }
    function getT: DatabaseType;

    procedure executeSQLNoWait(Q: TSQLQuery; withOpen : boolean);

  private
    procedure ConnectOracle;
    procedure ConnectDB2;

  var
    Conn: TSQLConnection;
    T: DatabaseType;

  end;

implementation

constructor DBConnect.Create(paramT: DatabaseType);
begin
  T := paramT;
  Conn := TSQLConnection.Create(nil);
end;

function DBConnect.getT;
begin
  result := T;
end;

procedure DBConnect.executeSQLNoWait(Q: TSQLQuery; withOpen : boolean);
var
  timeout: integer;
  QQ: TSQLQuery;
  inn : boolean;
begin
  case T of
    DB2:
      begin
        QQ := getQ('SELECT CURRENT LOCK  TIMEOUT AS C FROM DUAL');
        with QQ do
        begin
          ExecSql;
          Open;
          timeout := FieldByName('C').AsInteger;
          Close();
        end;
        QQ := getQ('SET CURRENT LOCK TIMEOUT 0');
        QQ.ExecSQL;
        QQ.Close;

        { In case of exception TIMEOUT will not be restored.
          Find better implementation for real environment. }

        {  Run statement now. }
        Q.ExecSQL;
        if (withOpen) then Q.Open;
        inn := Conn.InTransaction;

        { restore original value }
        QQ := getQ('SET CURRENT LOCK TIMEOUT ' + IntToStr(timeout));
        QQ.ExecSql;
        QQ.Close;
      end;
    Oracle:
      with Q do
      begin
        { IMPORTANT: run executeSQLNoWait only once for the statement. Find better
          solution for real environment. }
        SQL.Add(' NOWAIT');
        ExecSql();
      end;
  end;
end;

procedure DBConnect.ConnectOracle;
begin
  With Conn do
  begin
    Conn.DriverName := 'Oracle';
    Params.Values['USER_NAME'] := 'testuser';
    Params.Values['PASSWORD'] := 'testuser';
    Params.Values['DATABASE'] := 'think:1521/testdb';
  end;
End;

procedure DBConnect.ConnectDB2;
begin
  With Conn do
  begin
    DriverName := 'DB2';
    // VendorLib := 'db2cli.dll';
    // LibraryName := 'dbxdb2.dll';
    // GetDriverFunc := 'getSQLDriverDB2';
{
    Params.Values['USER_NAME'] := 'db2inst1';
    Params.Values['PASSWORD'] := 'db2inst1';
    Params.Values['DATABASE'] := 'asample';
}
    Params.Values['USER_NAME'] := 'db2inst3';
    Params.Values['PASSWORD'] := 'db2inst3';
    Params.Values['DATABASE'] := 'sample';
  end;
end;

{ Connect to database regarding database type (Oracle or DB2) }
procedure DBConnect.connect;
begin
  case T of
    DB2:
      ConnectDB2;
    Oracle:
      ConnectOracle;
  end;
  Conn.Open;
end;

{ Disconnect. }
procedure DBConnect.disconnect;
begin
  Conn.Close;
end;

function DBConnect.getSP(packName: String; procName: String): TSQLStoredProc;
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
        { For some reason attempting pl/sql like packages requires false. }
        ParamCheck := false;
    end;
    Params.Clear;
  end;
  result := SP;
end;

function DBConnect.getSP(procName: String): TSQLStoredProc;
begin
  result := getSP('', procName);
end;

function DBConnect.getQ(statement: String): TSQLQuery;
var
  Q: TSQLQuery;
begin
  Q := TSQLQuery.Create(nil);
  with Q do
  begin
    SQLConnection := Conn;
    SQL.Add(statement);
  end;
  result := Q;
end;

end.
