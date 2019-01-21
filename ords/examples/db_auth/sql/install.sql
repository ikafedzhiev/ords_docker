set define '^'
set termout on

define PWD       = '^1'

-- Create the schema to hold the stored proc. This account is not directly accessible
create user sample_plsql_app identified by L0ck3dAcc0unt password expire account lock;

-- create the application users
create user example_user1 identified by ^PWD;
create user example_user2 identified by ^PWD;
grant connect to example_user1;
grant connect to example_user2;

alter session set current_schema=sample_plsql_app;

-- define the stored procedure
create or replace procedure sample_proc as
 l_user varchar(255) := owa_util.get_cgi_env('REMOTE_USER');
begin
 htp.prn('<h1>Hello ' || l_user || '!</h1>');
end;
/

-- authorize users to access stored proc
grant execute on sample_plsql_app.sample_proc to example_user1;
grant execute on sample_plsql_app.sample_proc to example_user2;

quit
