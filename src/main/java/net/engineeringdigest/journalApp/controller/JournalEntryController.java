package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{username}") // Get journal entry for specific username
    public ResponseEntity<?> getAllJournalEntriesOfUsers(@PathVariable String username){
        User user = userService.findByUserName(username);
        List<JournalEntry> userJournalEntries = user.getJournalEntries();
        if(userJournalEntries!=null && !userJournalEntries.isEmpty()){
            return new ResponseEntity<>(userJournalEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{username}")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String username){
        try{
            journalEntryService.saveEntry(myEntry, username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED) ;
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST) ;
        }
    }
// check after this line
    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        if(journalEntry.isPresent()){
            return  new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<Void> deleteJournalEntryById(@PathVariable ObjectId myId){
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        if(journalEntry.isPresent()) {
            journalEntryService.deleteById(myId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry>  UpdateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry){
        JournalEntry old = journalEntryService.findById(id).orElse(null);
//        if(old!=null){
//            old.setTitle(newEntry.getTitle()!=null && newEntry.getTitle().isEmpty() ?old.getTitle():newEntry.getTitle());
//            old.setContent(newEntry.getContent()!=null && newEntry.getContent().isEmpty() ?old.getContent():newEntry.getContent());
//            journalEntryService.saveEntry(old, user);
//            return new ResponseEntity<>(old, HttpStatus.CREATED);
//        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
