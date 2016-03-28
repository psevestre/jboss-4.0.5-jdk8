If installing in a version before JBoss 4.0.4, you should leave javassist.jar and common-softvaluehashmap.jar in place in this folder.
If installing in jboss 4.0.4 or later, the classes contained in javassist.jar and common-softvaluehashmap.jar will already be available,
and you should delete these files to avoid versioning conflicts.