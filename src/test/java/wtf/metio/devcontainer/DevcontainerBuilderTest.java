package wtf.metio.devcontainer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DevcontainerBuilderTest {

  @Test
  void createInstanceWithBuilder() {
    final var devcontainer = DevcontainerBuilder.builder().name("test").create();

    Assertions.assertEquals("test", devcontainer.name());
  }

}
