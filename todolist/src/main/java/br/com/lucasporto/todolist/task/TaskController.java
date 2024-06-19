package br.com.lucasporto.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lucasporto.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;
  
  @SuppressWarnings("rawtypes")
  @PostMapping("/")
  public ResponseEntity create( @RequestBody TaskModel TaskModel, HttpServletRequest request){
    var idUser = request.getAttribute("idUser");
    TaskModel.setIdUser((UUID) idUser);

    var currentDate = LocalDateTime.now();

    if(currentDate.isAfter(TaskModel.getStartAt()) || currentDate.isAfter(TaskModel.getEndAt())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio / data de término deve ser maior que a data atual");
    }
    if(TaskModel.getStartAt().isAfter(TaskModel.getEndAt())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a data de termino");
    }
    var task = this.taskRepository.save(TaskModel);
    return ResponseEntity.status(HttpStatus.OK).body(task);
  }

  @GetMapping("/")
  public List<TaskModel> list( HttpServletRequest request){
    var idUser = request.getAttribute("idUser");
    var tasks = this.taskRepository.findByIdUser((UUID)idUser);
    return tasks;
  }
  @PutMapping("/{id}")
  public TaskModel update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){

    var task = this.taskRepository.findById(id).orElse(null);
    
    Utils.copyNonNullProperties(taskModel, task);

    return this.taskRepository.save(task);
  }
}
