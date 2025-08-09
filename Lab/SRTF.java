import java.util.*;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int turnaroundTime;
    int waitingTime;
    int responseTime;
    boolean isStarted;
    boolean completed;

    Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime; 
        this.isStarted = false;
        this.completed = false;
    }
}

public class SRTF {
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

        int completed = 0;
        int currentTime = 0;
        float totalWT = 0, totalTAT = 0, totalRT = 0;

        List<Integer> ganttTime = new ArrayList<>();
        List<String> ganttProcess = new ArrayList<>();

        Process currentProcess = null;

        while (completed < n) {
            int idx = -1;
            int minRemainingTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                Process p = processes[i];
                if (!p.completed && p.arrivalTime <= currentTime && p.remainingTime < minRemainingTime) {
                    minRemainingTime = p.remainingTime;
                    idx = i;
                } else if (!p.completed && p.arrivalTime <= currentTime && p.remainingTime == minRemainingTime) {
                    if (p.arrivalTime < processes[idx].arrivalTime) {
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                if (ganttProcess.isEmpty() || !ganttProcess.get(ganttProcess.size() - 1).equals("Idle")) {
                    ganttProcess.add("Idle");
                    ganttTime.add(currentTime);
                }
                currentTime++;
            } else {
                Process p = processes[idx];
                if (currentProcess != p) {
                    ganttProcess.add("P" + p.pid);
                    ganttTime.add(currentTime);
                    currentProcess = p;
                }

                if (!p.isStarted) {
                    p.responseTime = currentTime - p.arrivalTime;
                    p.isStarted = true;
                }

                p.remainingTime--;
                currentTime++;

                if (p.remainingTime == 0) {
                    p.completed = true;
                    p.completionTime = currentTime;
                    p.turnaroundTime = p.completionTime - p.arrivalTime;
                    p.waitingTime = p.turnaroundTime - p.burstTime;

                    totalWT += p.waitingTime;
                    totalTAT += p.turnaroundTime;
                    totalRT += p.responseTime;

                    completed++;
                }
            }
        }

        ganttTime.add(currentTime);

        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT\tRT");
        for (Process p : processes) {
            System.out.printf("P%d\t%d\t%d\t%d\t%d\t%d\t%d\n",
                    p.pid, p.arrivalTime, p.burstTime,
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

        for (int time : ganttTime) {
            System.out.print(time + "\t");
        }
        System.out.println();
    }
}
