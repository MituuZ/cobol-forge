# Changelog

## 0.3.0

- Implement go-to-definition for COPY statements.
  - Searches files in the same way as the quick documentation feature.

## 0.2.0

- Removed hover info; COPY file preview is now shown via Quick Documentation (Ctrl+Q / Shift+K with IdeaVim).
- Automatically treats all files under a directory named `cobol` (case-insensitive) as COBOL file type.
- Automatically treats these extensions as COBOL files: `.cbl`, `.cob`, `.cpy`, `.cobol`.
- Parses `COPY` case-insensitively.
