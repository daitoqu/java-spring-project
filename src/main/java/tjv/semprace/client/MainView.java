package tjv.semprace.client;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import tjv.semprace.server.dto.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Route
public class MainView extends HorizontalLayout {
    @Autowired
    ClientUserService clientUserService;
    @Autowired
    ClientPostService clientPostService;
    @Autowired
    ClientCommentService clientCommentService;

    Notification notification = new Notification("Text", 3000);

    //Grids with data
    List<UserDTO> usersList = new ArrayList<>();
    Grid<UserDTO> usersGrid = new Grid<>();
    List<CommentDTO> commentsList = new ArrayList<>();
    Grid<CommentDTO> commentsGrid = new Grid<>();
    List<PostDTO> postsList = new ArrayList<>();
    Grid<PostDTO> postsGrid = new Grid<>();

    //User menu
    NumberField userID = new NumberField("User ID");
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");

    NumberField friendFirstID = new NumberField("First user ID");
    NumberField friendSecondID = new NumberField("Second user ID");

    //Post menu
    NumberField postID = new NumberField("Post ID");
    NumberField postAuthorID = new NumberField("Post author ID");
    TextArea postContent = new TextArea("Post content");

    //Comment menu
    NumberField commentID = new NumberField("Comment ID");
    NumberField commentAuthorID = new NumberField("Comment author ID");
    NumberField commentRootID = new NumberField("Comment root ID");
    TextArea commentContent = new TextArea("Comment content");

    public MainView() {
        setUpDataGrids();
        setUpUserMenu();
    }

    private void addTestData() {
        try {
            clientUserService.create(new UserCreateNewDTO("Name 1", "Last name 1"));
            clientUserService.create(new UserCreateNewDTO("Name 2", "Last name 2"));
            clientUserService.create(new UserCreateNewDTO("Name 3", "Last name 3"));
            clientUserService.addFriend(1, 2);
            clientUserService.addFriend(1, 3);
            clientPostService.create(new PostCreateDTO(1, "Post 1 from user 1"));
            clientPostService.create(new PostCreateDTO(3, "Post 2 from user 3"));
            clientCommentService.create(new CommentCreateDTO(1, 4, "Comment 1 for post 1 from user 1"));
            clientCommentService.create(new CommentCreateDTO(2, 4, "Comment 2 for post 1 from user 2"));
            clientCommentService.create(new CommentCreateDTO(3, 4, "Comment 3 for post 1 from user 3"));
            clientCommentService.create(new CommentCreateDTO(1, 5, "Comment 1 for post 2 from user 1"));
            clientCommentService.create(new CommentCreateDTO(2, 5, "Comment 2 for post 2 from user 2"));
            clientCommentService.create(new CommentCreateDTO(3, 5, "Comment 3 for post 2 from user 3"));
            updateAll();
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
    }

    public void setUpUserMenu() {
        HorizontalLayout userTextFields = new HorizontalLayout(userID, firstName, lastName);
        HorizontalLayout userButtons = new HorizontalLayout(
                new Button("New", event -> addUser()),
                new Button("Find", event -> findUser()),
                new Button("Edit", event -> editUser()),
                new Button("Delete", event -> delUser())
        );

        HorizontalLayout friendMenuTextFields = new HorizontalLayout(friendFirstID, friendSecondID);
        HorizontalLayout friendMenuButtons = new HorizontalLayout(
                new Button("Add friend", event -> addFriend()),
                new Button("Remove friend", event -> delFriend())
        );

        HorizontalLayout postTextFields = new HorizontalLayout(postID, postAuthorID, postContent);
        HorizontalLayout postButtons = new HorizontalLayout(
                new Button("New", event -> addPost()),
                new Button("Find", event -> findPost()),
                new Button("Edit", event -> editPost()),
                new Button("Delete", event -> delPost())
        );

        HorizontalLayout commentTextFields = new HorizontalLayout(commentID, commentAuthorID, commentRootID, commentContent);
        HorizontalLayout commentButtons = new HorizontalLayout(
                new Button("New", event -> addComment()),
                new Button("Find", event -> findComment()),
                new Button("Edit", event -> editComment()),
                new Button("Delete", event -> delComment())
        );
        add(new VerticalLayout(userTextFields, userButtons, friendMenuTextFields, friendMenuButtons, postTextFields, postButtons, commentTextFields, commentButtons));
    }

    public void editPost() {
        if (postID.isEmpty() || postAuthorID.isEmpty() || postContent.isEmpty()) {
            notification.setText("Error: One of the fields is empty.");
            notification.open();
            return;
        }
        PostCreateDTO newPost = new PostCreateDTO(postAuthorID.getValue().intValue(), postContent.getValue());
        try {
            clientPostService.edit(postID.getValue().intValue(), newPost);
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
        updateAll();
    }

    public void findPost() {
        if (postID.isEmpty()) {
            notification.setText("Error: Post ID field is empty.");
            notification.open();
            return;
        }
        try {
            PostDTO find = clientPostService.get(postID.getValue().intValue());
            postAuthorID.setValue(find.getAuthorId().doubleValue());
            postContent.setValue(find.getContent());
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
    }

    public void addPost() {
        if (postAuthorID.isEmpty() || postContent.isEmpty()) {
            notification.setText("Error: One of the fields is empty.");
            notification.open();
            return;
        }
        PostCreateDTO newPost = new PostCreateDTO(postAuthorID.getValue().intValue(), postContent.getValue());
        try {
            clientPostService.create(newPost);
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
        updateAll();
    }

    public void delPost() {
        if (postID.isEmpty()) {
            notification.setText("Error: Post ID field is empty.");
            notification.open();
            return;
        }
        try {
            clientPostService.del(postID.getValue().intValue());
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
        updateAll();
    }

    public void editComment() {
        if (commentID.isEmpty() || commentAuthorID.isEmpty() || commentRootID.isEmpty() || commentContent.isEmpty()) {
            notification.setText("Error: One of the fields is empty.");
            notification.open();
            return;
        }
        CommentCreateDTO newComment = new CommentCreateDTO(
                commentAuthorID.getValue().intValue(), commentRootID.getValue().intValue(), commentContent.getValue()
        );
        try {
            clientCommentService.edit(commentID.getValue().intValue(), newComment);
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
        updateAll();
    }

    public void findComment() {
        if (commentID.isEmpty()) {
            notification.setText("Error: Comment ID field is empty.");
            notification.open();
            return;
        }
        try {
            CommentDTO find = clientCommentService.get(commentID.getValue().intValue());
            commentAuthorID.setValue(find.getAuthorId().doubleValue());
            commentRootID.setValue(find.getRootPostId().doubleValue());
            commentContent.setValue(find.getContent());
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
    }

    public void addComment() {
        if (commentAuthorID.isEmpty() || commentRootID.isEmpty() || commentContent.isEmpty()) {
            notification.setText("Error: One of the fields is empty.");
            notification.open();
            return;
        }
        CommentCreateDTO newComment = new CommentCreateDTO(
                commentAuthorID.getValue().intValue(), commentRootID.getValue().intValue(), commentContent.getValue()
        );
        try {
            clientCommentService.create(newComment);
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
        updateAll();
    }

    public void delComment() {
        if (commentID.isEmpty()) {
            notification.setText("Error: Comment ID field is empty.");
            notification.open();
            return;
        }
        try {
            clientCommentService.del(commentID.getValue().intValue());
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
        updateAll();
    }

    public void editUser() {
        if (firstName.isEmpty() || lastName.isEmpty() || userID.isEmpty()) {
            notification.setText("Error: One of the fields is empty.");
            notification.open();
            return;
        }
        UserCreateNewDTO newUser = new UserCreateNewDTO(firstName.getValue(), lastName.getValue());
        try {
            clientUserService.edit(userID.getValue().intValue(), newUser);
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
        updateAll();
    }

    public void findUser() {
        if (userID.isEmpty()) {
            notification.setText("Error: User ID field is empty.");
            notification.open();
            return;
        }
        try {
            UserDTO find = clientUserService.get(userID.getValue().intValue());
            firstName.setValue(find.getFirstName());
            lastName.setValue(find.getLastName());
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
    }

    public void addUser() {
        if (firstName.isEmpty() || lastName.isEmpty()) {
            notification.setText("Error: One of the fields is empty.");
            notification.open();
            return;
        }
        UserCreateNewDTO newUser = new UserCreateNewDTO(firstName.getValue(), lastName.getValue());
        try {
            clientUserService.create(newUser);
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
        updateAll();
    }

    public void delUser() {
        if (userID.isEmpty()) {
            notification.setText("Error: User ID field is empty.");
            notification.open();
            return;
        }
        try {
            clientUserService.del(userID.getValue().intValue());
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
        updateAll();
    }

    public void addFriend() {
        if (friendFirstID.isEmpty() || friendSecondID.isEmpty()) {
            notification.setText("Error: User ID field is empty.");
            notification.open();
            return;
        }
        try {
            clientUserService.addFriend(friendFirstID.getValue().intValue(), friendSecondID.getValue().intValue());
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
        updateAll();
    }

    public void delFriend() {
        if (friendFirstID.isEmpty() || friendSecondID.isEmpty()) {
            notification.setText("Error: User ID field is empty.");
            notification.open();
            return;
        }
        try {
            clientUserService.delFriend(friendFirstID.getValue().intValue(), friendSecondID.getValue().intValue());
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
        updateAll();
    }

    public void updateComments() {
        try {
            Collection<CommentDTO> comments = clientCommentService.getAll();
            commentsList.clear();
            commentsList.addAll(comments);
            commentsGrid.getDataProvider().refreshAll();
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
    }

    public void updateUsers() {
        try {
            Collection<UserDTO> users = clientUserService.getAll();
            usersList.clear();
            usersList.addAll(users);
            usersGrid.getDataProvider().refreshAll();
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
    }

    public void updatePosts() {
        try {
            Collection<PostDTO> posts = clientPostService.getAll();
            postsList.clear();
            postsList.addAll(posts);
            postsGrid.getDataProvider().refreshAll();
        } catch (Exception e) {
            notification.setText(e.getMessage());
            notification.open();
        }
    }

    public void updateAll() {
        updateUsers();
        updatePosts();
        updateComments();
    }

    public void setUpDataGrids() {
        VerticalLayout displayData = new VerticalLayout();
        displayData.add(new Button("Update", event -> updateAll()));
        displayData.add(new Button("Test data", event -> addTestData()));

        usersGrid.addColumn(UserDTO::getId).setHeader("ID").setSortable(true);
        usersGrid.addColumn(UserDTO::getFirstName).setHeader("First name").setSortable(true);
        usersGrid.addColumn(UserDTO::getLastName).setHeader("First name").setSortable(true);
        usersGrid.addColumn(UserDTO::getFriendsIds).setHeader("Friends IDs");
        usersGrid.setHeightByRows(true);
        usersGrid.setItems(usersList);

        postsGrid.addColumn(PostDTO::getId).setHeader("ID").setSortable(true);
        postsGrid.addColumn(PostDTO::getAuthorId).setHeader("Author ID").setSortable(true);
        postsGrid.addColumn(PostDTO::getContent).setHeader("Content").setSortable(true);
        postsGrid.setHeightByRows(true);
        postsGrid.setItems(postsList);

        commentsGrid.addColumn(CommentDTO::getId).setHeader("ID").setSortable(true);
        commentsGrid.addColumn(CommentDTO::getAuthorId).setHeader("Author ID").setSortable(true);
        commentsGrid.addColumn(CommentDTO::getRootPostId).setHeader("Root post ID").setSortable(true);
        commentsGrid.addColumn(CommentDTO::getContent).setHeader("Content").setSortable(true);
        commentsGrid.setHeightByRows(true);
        commentsGrid.setItems(commentsList);

        displayData.add(usersGrid, postsGrid, commentsGrid);

        add(displayData);
    }
}