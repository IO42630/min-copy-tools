# min-copy-tools

== Overview

A collection of tools to help manage files.

[cols="1,3"]
|===
| *Name* | *Description*
| HashCopyApp | Copy files from `src` to `dir`, using a `hash` as mapping.
| PathCopyApp | Copy files from `src` to `dir`, using a `path` as mapping.
|===

== DeleteEmptyDirApp

* Walks the flie tree, looking for empty directories
* Deletes them
* Runs in a loop until no more empty directories are found

== CopyApp

* provide `src` and `dst` root
* walk the `src` file tree (locking files as appropriate)
** IF file in target does not exist , THEN `move`
** ELSE : calc both checksum
*** IF : checksum matches (~same file)
**** IF : `src` older -> reduce `dst` timestamp
**** IN ALL CASES : DEL `src`
*** ELSE (~different file)
**** IF : `dst` older
***** THEN : MOVE
**** ELSE : NOP (manual rework)
