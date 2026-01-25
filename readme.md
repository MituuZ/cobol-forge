# COBOL Forge

A minimal IntelliJ plugin providing rudimentary COBOL support.

## Features

- COPY file preview via Quick Documentation (Ctrl+Q / Shift+K with IdeaVim).
- Automatically treats all files under a directory named `cobol` (case-insensitive) as COBOL file type.
- Automatically treats these extensions as COBOL files: `.cbl`, `.cob`, `.cpy`, `.cobol`.
- Highlights COPY keywords.
- Case-insensitive parsing for `COPY`.

## File type detection

COBOL is recognized automatically for:

- Any file under a directory named `COBOL`
- File extensions: `.cbl`, `.cob`, `.cpy`, `.cobol`

## COPY lookup behavior

COPY files are resolved by searching the project for `<name>` with the following suffixes: `.cbl, .cob, .cpy, .cobol`
and no extension.  
Matching/parsing of the `COPY` keyword is case-insensitive.
