#!/bin/bash
#save stdin
cat > tmp7987978564456432113.txt
#this is really a c script
printf "#include <stdio.h>\nint main(){int tmp, hi, lo; while((tmp=getchar())>=0) {hi=(tmp>>4)&15; lo=tmp&15; if (hi<10) putchar((char)(hi+'0')); else putchar((char)(hi-10+'A')); if (lo<10) putchar((char)(lo+'0')); else putchar((char)(lo-10+'A')); } return 0;}" > tmp7987978564456432113.c
gcc tmp7987978564456432113.c -o a7987978564456432113.out
chmod +x a7987978564456432113.out
#run the c program
cat tmp7987978564456432113.txt | ./a7987978564456432113.out > a.hp8
#cleanup
rm tmp7987978564456432113.c
rm tmp7987978564456432113.txt
rm a7987978564456432113.out

