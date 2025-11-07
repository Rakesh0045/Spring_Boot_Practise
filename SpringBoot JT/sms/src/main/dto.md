
### 💡 Quick Overview of the Flow:

```
Client Request → Controller → Service → Repository → DB
DB Response → Repository → Service → DTO → Controller → Client Response
```

### 📦 Entities vs DTO:

* **Entity**: This maps directly to your database table (e.g., `Student`).
* **DTO (Data Transfer Object)**: Used to send/receive data between client and server (API) in a clean, secure way (e.g., `StudentDTO`).
* Avoids sending sensitive or unnecessary fields from Entity.

---

## ✅ 1. Get All Students (`@GetMapping`)

### ➤ `StudentController`:

```java
@GetMapping
public List<StudentDTO> getStudents() {
    return studentService.getStudents();
}
```

### ➤ `StudentService`:

```java
public List<StudentDTO> getStudents() {
    return studentRepository.findAll()
            .stream()
            .map(student -> {
                StudentDTO dto = new StudentDTO();
                BeanUtils.copyProperties(student, dto);
                return dto;
            })
            .toList();
}
```

### 🔄 Flow:

1. Client hits `GET /api/v1/students`.
2. Controller calls `studentService.getStudents()`.
3. Service fetches all students from DB (`findAll()`).
4. Converts each `Student` to `StudentDTO` using `BeanUtils.copyProperties()`.
5. Returns the list of DTOs to the controller → client.

---

## ✅ 2. Get Student by ID (`@GetMapping("/{id}")`)

### ➤ `StudentController`:

```java
@GetMapping("/{id}")
public StudentDTO getStudent(@PathVariable String id) {
    return studentService.getStudentById(id);
}
```

### ➤ `StudentService`:

```java
public StudentDTO getStudentById(String id) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
    
    StudentDTO dto = new StudentDTO();
    BeanUtils.copyProperties(student, dto);
    return dto;
}
```

### 🔄 Flow:

1. Client hits `GET /api/v1/students/{id}`.
2. Controller passes `id` to the service.
3. Service fetches student by ID (or throws exception if not found).
4. Converts `Student` to `StudentDTO`.
5. Returns DTO → controller → client.

---

## ✅ 3. Create New Student (`@PostMapping`)

### ➤ `StudentController`:

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public StudentDTO createStudent(@Valid @RequestBody StudentDTO newStudentDTO) {
    Student newStudent = new Student();
    BeanUtils.copyProperties(newStudentDTO, newStudent);
    return studentService.saveStudent(newStudent);
}
```

### ➤ `StudentService`:

```java
public StudentDTO saveStudent(Student newStudent) {
    Student savedStudent = studentRepository.save(newStudent);
    StudentDTO dto = new StudentDTO();
    BeanUtils.copyProperties(savedStudent, dto);
    return dto;
}
```

### 🔄 Flow:

1. Client sends `POST` request with `StudentDTO` (JSON body).
2. Controller maps JSON → DTO (automatically via `@RequestBody`).
3. Validates DTO (`@Valid`), then maps DTO → Entity.
4. Service saves entity to DB.
5. After saving, service maps saved entity back to DTO and returns to client.

---

## ✅ 4. Delete Student by ID (`@DeleteMapping`)

### ➤ `StudentController`:

```java
@DeleteMapping("/delete/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteStudent(@PathVariable String id) {
    studentService.deleteStudentById(id);
}
```

### ➤ `StudentService`:

```java
public void deleteStudentById(String id) {
    StudentDTO student = getStudentById(id); // will throw exception if not found
    Student entity = new Student();
    BeanUtils.copyProperties(student, entity);
    studentRepository.delete(entity);
}
```

### 🔄 Flow:

1. Client hits `DELETE /api/v1/students/delete/{id}`.
2. Controller calls service.
3. Service checks if student exists using `getStudentById()`.
4. If found, deletes the student from DB.

✅ Improvement: Instead of converting `StudentDTO` to entity, use repository’s `.deleteById(id)` directly for simplicity.

---

## ✅ 5. Update Student by ID (`@PutMapping`)

### ➤ `StudentController`:

```java
@PutMapping("/update/{id}")
public StudentDTO updateStudent(@PathVariable String id, @RequestBody StudentDTO studentDTO) {
    return studentService.updateStudentById(id, studentDTO);
}
```

### ➤ `StudentService`:

```java
public StudentDTO updateStudentById(String id, StudentDTO studentDTO) {
    Student updatedStudent = new Student();
    updatedStudent.setStudentId(id); // Reuse the same ID
    BeanUtils.copyProperties(studentDTO, updatedStudent);
    Student saved = studentRepository.save(updatedStudent);
    BeanUtils.copyProperties(saved, studentDTO);
    return studentDTO;
}
```

### 🔄 Flow:

1. Client hits `PUT /api/v1/students/update/{id}` with new DTO data.
2. Controller passes ID and DTO to service.
3. Service creates new entity, sets same ID (to perform update), copies DTO → entity.
4. Saves updated entity in DB.
5. Converts back to DTO → returns to controller → client.

✅ Tip: It’s better to fetch the existing entity first, update fields, and save to avoid overwriting missing fields.

---

## 🎯 Summary of DTO Purpose:

* Prevents overexposing your DB schema.
* Cleaner API response and request models.
* Helps with validation (`@Valid` annotations work with DTO).
* Allows flexibility in shaping the API.

---

Would you like me to:

* Improve this code (e.g., update method to avoid full overwrite)?
* Help you create a Mapper class or use MapStruct for clean conversion?
* Add pagination or sorting in `getStudents()`?

Let me know what you want to build next!
