package online.pigeonshouse.minirpc.framwork;

import lombok.Data;
import online.pigeonshouse.minirpc.service.MiniObject;

@Data
public class Parameter {
  private String name;
  private MiniObject value;

  public Parameter(String name, MiniObject value) {
    this.name = name;
    this.value = value;
  }
}