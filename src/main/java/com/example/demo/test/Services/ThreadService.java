package com.example.demo.test.Services;
import com.example.demo.test.Models.Thread;
import com.example.demo.test.Repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ThreadService {
    @Autowired
    ThreadRepository threadRepository;

    public ThreadService()
    {
    }
    public void intilaize()
   {
        Thread threads[] = new Thread[3];
        for(int i=0; i< 3 ;i++)
        {
        threads[i] = new Thread();
        threads[i].setBusy(false);
        threads[i].setTask(null);
        threadRepository.save(threads[i]);
        }
   }
    public List<Thread> findByBusy(boolean b)
    {
       return threadRepository.findByBusy(b) ;
    }
    public void save(Thread currentThread)
    {
        threadRepository.save(currentThread);
    }
    public Optional<Thread> findById(long threadId) {
       return threadRepository.findById(threadId) ;
    }
    public Long count() {
        return threadRepository.count() ;
    }
}
