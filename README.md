# MapSerialize

This is a class (MapSeralize) and set of annotations (MapRename and MapIgnore) that work together to convert
a given class into a shallow map (one level).  Primitive fields are cast to an `Object` which then places
them in the appopriate wrapper class (an `int` becomes an `Integer` and the like).