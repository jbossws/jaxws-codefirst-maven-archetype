package ${package};

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HelloWorldIntegrationTest {

    @Test
    public void testSayHi() throws Exception {
        Service service = Service.create(new URL("http://localhost:8080/jaxws-codefirst/HelloWorldService?wsdl"), new QName("http://hello.world.ns/", "HelloWorldService"));
        HelloWorld port = service.getPort(new QName("http://hello.world.ns/", "HelloWorldPort"), HelloWorld.class);
        Assert.assertEquals("Hello John", port.sayHi("John"));
    }
}
