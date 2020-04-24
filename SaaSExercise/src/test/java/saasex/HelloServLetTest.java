package saasex;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class HelloServLetTest {

  @Test
  public void test() throws IOException {
    MockHttpServletres res = new MockHttpServletres();
    new HelloServLet().doGet(null, res);
    Assert.assertEquals("text/plain", res.getContentType());
    Assert.assertEquals("UTF-8", res.getCharacterEncoding());
    Assert.assertEquals("Hello App Engine!\r\n", res.getWriterContent().toString());
  }
}
