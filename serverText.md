Marytts server module and text

1. 服务端启动

Mary.java是入口。 先调用startup加载对应处理模块，记录在ModuleRegistry内。在判断服务端类型（socket：MaryServer, http：MaryhttpServer, command line）， 然后实例化对应的类。再调用对应内的run进入处理。



2. 对应每个server类型实际上是将输入请求转化为SynthesisRequestHandler（或InfoRequestHandler）可接受的方式。再调用Request， Request调用ModuleRegistry获取处理模块，从而对输入进行处理。
