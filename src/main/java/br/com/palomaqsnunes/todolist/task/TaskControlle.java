package br.com.palomaqsnunes.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.palomaqsnunes.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskControlle {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity<String> create(@RequestBody TaskModel taskmodel, HttpServletRequest request){
        taskmodel.setUserId((UUID)request.getAttribute("userId"));
        
        var currDate = LocalDateTime.now();
        if(currDate.isAfter(taskmodel.getStarDate()) || currDate.isAfter(taskmodel.getEnDate())){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Date invalid");
        }
        if(taskmodel.getEnDate().isBefore(taskmodel.getStarDate())){
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EndDate invalid");
        }else{
                this.taskRepository.save(taskmodel);
                return  ResponseEntity.status(HttpStatus.OK).body("Created");
        }
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        
        var userid = request.getAttribute("userId");
        var taskList = this.taskRepository.findByUserId((UUID) userid);
        return taskList;
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request){

        var userid = request.getAttribute("userId");
        
        var currTask = this.taskRepository.findById(id).orElse(null);

        if(currTask==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task don't exists");
        }
        if(currTask.getUserId().equals(userid)){
            taskModel.setUserId((UUID) userid);
            Utils.copyNumm(taskModel, currTask);
            this.taskRepository.save(currTask);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You can't  change this task");
        }
        
      

    }



}
