# Android xml中@+id和@id的区别

@+id会在R.id文件中创建一个新的引用

@id是引用已经在工程中创建过的id



除了创建新引用外，其他的一律使用@id，否则在findViewById的时候会造成一定的困扰，不知道哪个才是创建新id的地方。

