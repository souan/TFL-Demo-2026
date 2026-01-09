package lu.cnfpcfullstackdev.tfl_api.controller;

import lu.cnfpcfullstackdev.tfl_api.entity.TflUser;
import lu.cnfpcfullstackdev.tfl_api.service.TflUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class TflUserController {

    @Autowired
    private TflUserService userService;

    // GET all users
    @GetMapping
    public ResponseEntity<List<TflUser>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<TflUser> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // GET all businesses (useful for dropdown in frontend)
    @GetMapping("/businesses")
    public ResponseEntity<List<TflUser>> getAllBusinesses() {
        return ResponseEntity.ok(userService.getAllBusinesses());
    }

    // POST create user
    @PostMapping
    public ResponseEntity<TflUser> createUser(@RequestBody TflUser user) {
        TflUser created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT update user
    @PutMapping("/{id}")
    public ResponseEntity<TflUser> updateUser(@PathVariable Long id, @RequestBody TflUser user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    // DELETE user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}