package 重学设计模式.Section2.单一职责原则.bad;

public class VideoUserService {
    public void serveGrade(String userType){
        if("VIP会员".equals(userType)){
            System.out.println("VIP会员、蓝光");
        }else if ("普通用户".equals(userType)){
            System.out.println("普通会员、720p");
        }else if("访客用户".equals(userType)){
            System.out.println("访客、480p");
        }
    }

//    如果想要添加新的用户，需要修改类中的if-else,每个改动都有可能影响其他情况。
    public void test_serveGrade(){
        serveGrade("VIP会员");
        serveGrade("普通用户");
        serveGrade("访客用户");
    }

    public static void main(String[] args) {
        new VideoUserService().test_serveGrade();
    }
}
