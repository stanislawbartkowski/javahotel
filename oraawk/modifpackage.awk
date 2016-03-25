@include "fun.awk"

{
  line = gensub(/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Pp][Aa][Cc][Kk][Aa][Gg][Ee]/,"CREATE OR REPLACE PACKAGE","g",$0)   
  printline(line)
}