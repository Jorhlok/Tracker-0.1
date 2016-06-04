USAGE

Windows:
type file | bytetohex.exe > a.hp8

*nix:
cat file | ./bytetohex.sh

"bytetohex.sh" is simply a wrapper for the C script 
that saves stdin to a file, the the C program, then compiles it with gcc, 
then runs it, then removes temporary files so it appears to be a self contained script.

In fact, it is able to be run by type "cat file | " into a console then pasting the 
contents of the script into the console. 

"HP8" stands for "Hexadecimal PCM 8-bit" but it's just ascii hexadecimal so you can use it for non audio data.

To get audio to raw 8-bit file, load it in Audacity, make it mono, then export to "other uncompressed",
then select RAW (header-less) and Unsigned 8-bit PCM. Alternatively you could save it as signed but
Tracker 0.1 wants it unsigned.

Remember that the resulting file will be twice as large as the original.
