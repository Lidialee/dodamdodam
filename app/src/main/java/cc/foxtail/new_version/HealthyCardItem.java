package cc.foxtail.new_version;

public class HealthyCardItem {

    private String id, healthyWhere, topButton, midButton, botButton, healthyDetail;
    private boolean selectable = true;

    public HealthyCardItem(String id, String healthyWhere, String topButton,
                           String midButton, String botButton, String healthyDetail) {
        this.id = id;
        this.healthyWhere = healthyWhere;
        this.topButton = topButton;
        this.midButton = midButton;
        this.botButton = botButton;
        this.healthyDetail = healthyDetail;
    }

    public boolean isSelectable(){
        return selectable;
    }
    public void setSelectable(boolean set){
        selectable = set;
    }
    public String getId() {return id;}
    public String getHealthyWhere() {return healthyWhere;}
    public String getTopButton() {return topButton;}
    public String getMidButton() {return midButton;}
    public String getBotButtone() {return botButton;}
    public String getHealthyDetail() {return healthyDetail;}

}
