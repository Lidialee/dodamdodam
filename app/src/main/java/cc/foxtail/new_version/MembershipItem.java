package cc.foxtail.new_version;


public class MembershipItem {

    private String id, name,number;

    private boolean selectable = true;

    public MembershipItem(String id, String name,String number ) {

        this.id = id;
        this.name = name;
        this.number = number;
    }

    public boolean isSelectable(){
        return selectable;
    }
    public void setSelectable(boolean set){
        selectable = set;
    }

    public String getnumber() {return number;}
    public String getname() {return name;}
    public String getId() {return id;}

}
