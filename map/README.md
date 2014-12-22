此文件夹用来保存地图文件"connection.csv"和"country.csv"。系统提供的默认地图包含五个国家和国家之间的11条连接

"country.csv"文件用于存储国家，每个国家需要有四个属性，分别为"name","location","population","density"。
国家不能有重名，属性值的具体设置方式参见"country.csv"样例及设计文档

"connection.csv"文件用于存储国家之间的连接，每个连接需要有四个属性，分别为"type","country1","country2","property"。
其中，"type"为连接种类，可为"air","land","ocean"三种；"country1","country2"为连接两端的国家名称；
"property"为国家之间交流的频繁程度，用户可以自行设置。一般来说，"land"类型的连接交流最频繁，"air"类型次之，"ocean"类型最低。
具体值的设置请参看"connection.csv"样例

程序暂时没有实现对病毒属性的文件式修改。如需修改，请找到./src/plagueWorld/spread/Spread.java文件，对其中的六个static
变量进行修改。每个属性的值均为[0,3]之间整数。