package core2.chapter05b.demo10;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


class HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        String name = jobDetail.getJobDataMap().getString("name");
        System.out.println("Hello from " + name + " at " + new Date());
    }
}

class QuartzTest {
    public static void main(String[] args) {
        try {
            // 1. create a default scheduler.
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // 2. create a trigger
            Trigger trigger = newTrigger().withIdentity("Trigger1", "Group1")
                    .startNow()
                    .withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever())
                    .build();

            // 3. create a job detail
            JobDetail jobDetail = newJob(HelloJob.class)
                    .withIdentity("job1", "group1")
                    .usingJobData("name", "quartz")
                    .build();

            // 4. bind the job and trigger and launch
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();

            Thread.sleep(5000);
            scheduler.shutdown(true);

        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
