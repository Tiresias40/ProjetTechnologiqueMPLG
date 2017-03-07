package exempleJson;

public class Object {
  public String object_classe;
  public String object_name;
  
  public Object() {
    this.object_classe = "class";
    this.object_name = "name";
  }
  
  public String getClasse() {
    return object_classe;
  }
  
  public void setClass(String classe) {
    this.object_classe = classe;
  }
  
  public String getName() {
    return this.object_name;
  }
  
  public void setName(String name) {
    this.object_name = name;
  }
  
  public String toString() {
    return ("[Object] classe = " + getClasse() + "; name = " + getName());
  }
  
  

}
