# COBOL Forge
A minimal IntelliJ plugin providing rudimentary COBOL support.

## Features
Hoverable inlay hints to peek COPY files. Files are looked up from file index and searched with the following extensions: (and no extension)

`.cbl, .cob, .cpy, .cobol`

Highlights COPY keywords.
Quick documentation (Ctrl+Q) shows full COPY file contents.

## File extensions
By default, the COBOL file type is registered for: `.cbl, .cob, .cpy, .cobol`.  
If your project uses different extensions, register them manually in IntelliJ (Settings -> Editor -> File Types).

## COPY lookup behavior
COPY files are resolved by searching the project for `<name>` with the following suffixes: `.cbl, .cob, .cpy, .cobol` and no extension.  
If your COPY files use other extensions, they may not be found even if the file type is registered.
