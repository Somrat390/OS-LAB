import java.util.*;

class Process {
    int Pid;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int turnaroundTime;
    int waitingTime;
    int responseTime;
    boolean isStarted;
    boolean isCompleted;

    Process(int Pid, int arrivalTime, int burstTime) {
        this.Pid = Pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.isStarted = false;
        this.isCompleted = false;
    }
}

public class RR {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        Process[] processes = new Process[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time for Process " + (i + 1) + ": ");
            int at = sc.nextInt();
            System.out.print("Enter burst time for Process " + (i + 1) + ": ");
            int bt = sc.nextInt();
            processes[i] = new Process(i + 1, at, bt);
        }

        System.out.print("Enter Time Quantum: ");
        int quantum = sc.nextInt();

        int completed = 0;
        int currentTime = 0;
        float totalWT = 0, totalTAT = 0, totalRT = 0;

        Queue<Integer> queue = new LinkedList<>();
        boolean[] inQueue = new boolean[n];

        List<Integer> ganttTimes = new ArrayList<>();
        List<String> ganttProcess = new ArrayList<>();

        
        for (int i = 0; i < n; i++) {
            if (processes[i].arrivalTime == 0) {
                queue.add(i);
                inQueue[i] = true;
            }
        }

        if (queue.isEmpty()) {
            ganttTimes.add(0);
            ganttProcess.add("Idle");
        }

        while (completed < n) {
            if (queue.isEmpty()) {
            
                if (ganttProcess.isEmpty() || !ganttProcess.get(ganttProcess.size() - 1).equals("Idle")) {
                    ganttProcess.add("Idle");
                    ganttTimes.add(currentTime);
                }
                currentTime++;
                
                for (int i = 0; i < n; i++) {
                    if (!inQueue[i] && !processes[i].isCompleted && processes[i].arrivalTime <= currentTime) {
                        queue.add(i);
                        inQueue[i] = true;
                    }
                }
                continue;
            }

            int idx = queue.poll();
            Process p = processes[idx];

            if (!p.isStarted) {
                p.responseTime = currentTime - p.arrivalTime;
                p.isStarted = true;
            }

            
            if (ganttProcess.isEmpty() || !ganttProcess.get(ganttProcess.size() - 1).equals("P" + p.Pid)) {
                ganttProcess.add("P" + p.Pid);
                ganttTimes.add(currentTime);
            }

            int execTime = Math.min(quantum, p.remainingTime);
            p.remainingTime -= execTime;
            currentTime += execTime;

            
            for (int i = 0; i < n; i++) {
                if (!inQueue[i] && !processes[i].isCompleted && processes[i].arrivalTime <= currentTime) {
                    queue.add(i);
                    inQueue[i] = true;
                }
            }

            if (p.remainingTime > 0) {
                queue.add(idx); 
            } else {
                p.isCompleted = true;
                completed++;
                p.completionTime = currentTime;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;

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
