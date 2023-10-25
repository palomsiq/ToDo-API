package br.com.palomaqsnunes.todolist.task;

import java.util.UUID;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")

public class TaskModel {
    
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    
    private String description;

    @Column(length = 50)
    private String title;
    private LocalDateTime starDate;
    private LocalDateTime enDate;
    private int priority;


    @CreationTimestamp
    private LocalDateTime createdAt;


    private UUID userId;

    public void setTitle(String title) throws Exception{
        if(title.length() > 50){
            throw new Exception("Title must have 50 char");
        }else{
            this.title = title;
        }
        
    }

}
