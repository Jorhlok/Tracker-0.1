#include <stdio.h>
int main(){
	int tmp, hi, lo; 
	while((tmp=getchar())>=0) {
		hi=(tmp>>4)&15; 
		lo=tmp&15; 
		if (hi<10) 
		putchar((char)(hi+'0')); 
		else putchar((char)(hi-10+'A')); 
		if (lo<10) putchar((char)(lo+'0')); 
		else putchar((char)(lo-10+'A')); 
	} 
	return 0;
}
