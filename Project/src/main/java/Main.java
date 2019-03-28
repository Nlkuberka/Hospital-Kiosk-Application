public class Main {
    public static void main(String[] args) {
        User.userID = "Guest";
        User.permissions = User.GUEST_PERMISSIONS;
        UIController controller = new UIController();
        controller.goToScene(UIController.LOGIN_MAIN);

        System.out.println("Collaborator is " + "X");
    }
}