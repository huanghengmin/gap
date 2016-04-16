package com.ichange.common.utils;

// ----------------------------------------------------------------------------

/**
 * This class provides a simple API for measuring and recording CPU usage. See
 * indivual methods for details. The class is abstract because it exposes a
 * completely static API.
 *
 * @author (C) 2002, Vladimir Roubtsov
 */
public abstract class OSMonitSystemInfo {
    public static final class CPUUsageSnapshot {
        public final long m_time, m_CPUTime;

        // constructor is private to ensure that makeCPUUsageSnapshot()
        // is used as the factory method for this class:
        private CPUUsageSnapshot(final long time, final long CPUTime) {
            m_time = time;
            m_CPUTime = CPUTime;
        }

    } // end of nested class

    // Custom exception class for throwing
    public static final class NegativeCPUTime extends Exception {
    }

    /**
     * Minimum time difference [in milliseconds] enforced for the inputs into
     * {@link #getProcessCPUUsage(OSMonitSystemInfo.CPUUsageSnapshot, OSMonitSystemInfo.CPUUsageSnapshot)}.
     * The motivation for this restriction is the fact that
     * <CODE>System.currentTimeMillis()</CODE> on some systems has a low
     * resolution (e.g., 10ms on win32). The current value is 100 ms.
     */
    public static final int MIN_ELAPSED_TIME = 100;

    /**
     * Creates a CPU usage data snapshot by associating CPU time used with
     * system time. The resulting data can be fed into
     * {@link #getProcessCPUUsage(OSMonitSystemInfo.CPUUsageSnapshot, OSMonitSystemInfo.CPUUsageSnapshot)}.
     */
    public static CPUUsageSnapshot makeCPUUsageSnapshot() throws OSMonitSystemInfo.NegativeCPUTime {
        long prCPUTime = getProcessCPUTime();
        if (prCPUTime < 0) throw new NegativeCPUTime();
        return new CPUUsageSnapshot(System.currentTimeMillis(), getProcessCPUTime());
    }

    /**
     * Computes CPU usage (fraction of 1.0) between <CODE>start.m_CPUTime</CODE>
     * and <CODE>end.m_CPUTime</CODE> time points [1.0 corresponds to 100%
     * utilization of all processors].
     *
     * @throws IllegalArgumentException if start and end time points are less than
     *                                  {@link #MIN_ELAPSED_TIME} ms apart.
     * @throws IllegalArgumentException if either argument is null;
     */
    public static double getProcessCPUUsage(final CPUUsageSnapshot start, final CPUUsageSnapshot end) {
        if (start == null) throw new IllegalArgumentException("null input: start");
        if (end == null) throw new IllegalArgumentException("null input: end");
        if (end.m_time < start.m_time + MIN_ELAPSED_TIME)
            throw new IllegalArgumentException("end time must be at least " + MIN_ELAPSED_TIME + " ms later than start time");

        return ((double) (end.m_CPUTime - start.m_CPUTime)) / (end.m_time - start.m_time);
    }

    /**
     * Returns the PID of the current process. The result is useful when you
     * need to integrate a Java app with external tools.
     */
    public static native int getProcessID();

    /**
     * Returns the number of processors on machine
     */
    public static native int getCPUs();

    /**
     * Returns CPU (kernel + user) time used by the current process [in
     * milliseconds]. The returned value is adjusted for the number of
     * processors in the system.
     */
    public static native long getProcessCPUTime();

    /**
     * Returns CPU (kernel + user) time used by the current process [in
     * perecents]. The returned value is either CPU percentage, or zero if this
     * is not supported by OS. Currently it is supported by Solaris8, and not
     * supported by Windows XP
     */
    public static native double getProcessCPUPercentage();

    /**
     * Returns maximum memory available in the system.
     */
    public static native long getMaxMem();

    /**
     * Returns current free memory in the system.
     */
    public static native long getFreeMem();

    /**
     * Returns system name info like "uname" command output
     */
    public static String getSysInfo() {
        return "";
    }

    /**
     * Returns CPU usage (fraction of 1.0) so far by the current process. This
     * is a total for all processors since the process creation time.
     */
    public static native double getProcessCPUUsage();

    /**
     * Returns current space allocated for the process, in Kbytes. Those pages
     * may or may not be in memory.
     */
    public static native long getMemoryUsage();

    /**
     * Returns current process space being resident in memory, in Kbytes.
     */
    public static native long getMemoryResident();

    /**
     * Sets the system native process PID for which all measurements will be
     * done. If this method is not called then the current JVM pid will act as a
     * default. Returns the native-dependent error code, or 0 in case of
     * success.
     */
    public static native int setPid(int pid);

    /**
     * Closes native-dependent process handle, if necessary.
     */
    public static native int detachProcess();

    // protected: .............................................................

    // package: ...............................................................

    // private: ...............................................................

    private OSMonitSystemInfo() {
    } // prevent subclassing

    private static final String SILIB = "osmonit";

    static {
        // loading a native lib in a static initializer ensures that it is
        // available done before any method in this class is called:
        try {
            System.loadLibrary(SILIB);
        } catch (UnsatisfiedLinkError e) {
            System.out.println("native lib '" + SILIB
                    + "' not found in 'java.library.path': "
                    + System.getProperty("java.library.path"));

            throw e; // re-throw
        }
    }

} // end of class
// ----------------------------------------------------------------------------
