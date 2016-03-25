{
  line = gensub(/ENABLE/,"","g",$0)   
  line = gensub(/NUMBER\(\*/,"NUMBER(31","g",line)
  if (line ~ /CONSTRAINT / && line !~ /ADD CONSTRAINT/) {
    print "--" line
    line = gensub(/CONSTRAINT \".+\"/,"" ,"g",line)
  }
  print line
}