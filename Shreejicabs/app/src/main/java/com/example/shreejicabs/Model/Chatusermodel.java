package com.example.shreejicabs.Model;

public class Chatusermodel {

    String Name;
    String id;

    public Chatusermodel(String name, String id) {
        Name = name;
        this.id = id;
    }

    public Chatusermodel() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public boolean equals(Object obj){

        if(!(obj instanceof Chatusermodel)){
            return false; //objects cant be equal
        }

        Chatusermodel anotherPerson = (Chatusermodel) obj;

        return this.getName().equals(anotherPerson.getName());

    }

    public int hashCode(){

        return this.getName().hashCode();

    }
}
