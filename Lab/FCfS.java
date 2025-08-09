import java.util.*;

class Process{
    int Pid;
    int arrivalTime;
    int burstTime;
    int completionTime;
    int turnaroundTime;
    int waitingTime;
    int responseTime;
    boolean isStarted;
    boolean isCompleted;

    Process(int Pid, int arrivalTime,int burstTime){
        this.Pid = Pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.isStarted = false;
        this.isCompleted = false;
    }
}

public class FCfS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter number of Process");
        int n = sc.nextInt();
        Process[] processes = new Process[n];

        for(int i = 0; i < n; i++) {
            System.out.println("Enter arrival time for Process" + (i+1) + ": ");
            int at = sc.nextInt();
            System.out.println("Enter burst time for Process" + (i+1) + ":");
            int bt = sc.nextInt();
            processes[i] = new Process(i+1,at,bt);
        }

        int completed = 0;
        int currentTime = 0;
        float totalWT = 0, totalTAT = 0, totalRT = 0;

        List<Integer> ganttTimes = new ArrayList<>();
        List<String> ganttProcess = new ArrayList<>();

        while (completed < n){
            int idx = -1;
            int earliestArrival = Integer.MAX_VALUE;
            for(int i = 0; i < n; i++){
                if(!processes[i].isCompleted && processes[i].arrivalTime <= currentTime){
                    idx = i;
                    earliestArrival = processes[i].arrivalTime;
                } else if (processes[i].arrivalTime == earliestArrival && processes[i].Pid < processes[idx].Pid) {
                        idx = i;
                }
            }

            if(idx == -1){
              if (ganttProcess.isEmpty() || !ganttProcess.get(ganttProcess.size() - 1).equals("Idle")) {
                    ganttProcess.add("Idle");
                    ganttTimes.add(currentTime);
                }
                currentTime++;
            }
            else{
                Process p = processes[idx];
                ganttTimes.add(currentTime);
                ganttProcess.add("P"+p.Pid);

                if(!p.isStarted){
                    p.responseTime = currentTime - p.arrivalTime;
                }

                currentTime += p.burstTime;
                p.completionTime = currentTime;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
                p.isCompleted = true;
                completed++;
                totalWT += p.waitingTime;
                totalTAT += p.turnaroundTime;
                totalRT += p.responseTime;
            }
        }
        ganttTimes.add(currentTime);
        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT\tRT");
        for (Process p : processes) {
            System.out.printf("P%d\t%d\t%d\t%d\t%d\t%d\t%d\n",
                    p.Pid, p.arrivalTime, p.burstTime,
                    p.completionTime, p.turnaroundTime, p.waitingTime, p.responseTime);
        }

        System.out.printf("\nAverage Waiting Time: %.2f\n", totalWT / n);
        System.out.printf("Average Turnaround Time: %.2f\n", totalTAT / n);
        System.out.printf("Average Response Time: %.2f\n", totalRT / n);

        System.out.println("\nGantt Chart:");
        for (String label : ganttProcess) {
            System.out.print("| " + label + " ");
        }
        System.out.println("|");

        for (int time : ganttTimes) {
            System.out.print(time + "\t");
        }
        System.out.println();
            
    }
}
    

