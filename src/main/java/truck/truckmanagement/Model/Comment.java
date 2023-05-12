package truck.truckmanagement.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String commentText;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) //orphanRemoval = true reiskia, jeigu trinsim parrent'a, issitrins ir vaikai
    private List<Comment> replies;
    @ManyToOne
    private Comment parentComment;
    @ManyToOne
    private Forum forum;
    @ManyToOne
    private User user;

    public Comment(String commentText, Comment parentComment, Forum forum, User user) {
        this.commentText = commentText;
        this.parentComment = parentComment;
        this.forum = forum;
        this.user = user;
    }

    @Override
    public String toString(){
        if(commentText.length() > 30)
            return commentText.substring(0, Math.min(commentText.length(), 30)) + "...";
        else
            return commentText.substring(0, Math.min(commentText.length(), 30));
    }
}
