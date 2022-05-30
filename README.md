# Programmieraufgabe-Huffman

Huffman Code implementation for a math module (mada)

## Compression

The inputfile "input.txt", that contains the sentence: "This is a test, to see if the encryption works!" is 47 bytes long. (47 ascii characters)
The outputfile "output.dat", that contains the huffman-code compression of said sentence, is only 23 bytes long.
That means we save 24 bytes by using the huffman-encoding. The problem is that we would also have to transmit the encoding table for the decoder.
Therefore the Huffman encoding is only efficient for larger file sizes, where the encoding table won't make a large difference in transmission size.

## Solution for output-mada.dat

Das Verfahren lohnt sich in der vorgestellten Form nur fuer Texte mit einer gewissen Laenge. Ansonsten ist der Overhead fuer die Erstellung der Tabelle zu gross.

Man koennte sich aber auch ueberlegen, die Tabelle geschickter abzuspeichern. Ansonsten: Gut gemacht
