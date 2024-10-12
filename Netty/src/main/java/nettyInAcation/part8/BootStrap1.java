package nettyInAcation.part8;

import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;

public class BootStrap1 extends AbstractBootStrap1 <BootStrap1,Channel>{
}

abstract class AbstractBootStrap1<B extends  AbstractBootStrap1<B,C>,C extends Channel>{
}

class ServerBootStrap1 extends AbstractBootStrap1<ServerBootStrap1, ServerChannel>{
}
