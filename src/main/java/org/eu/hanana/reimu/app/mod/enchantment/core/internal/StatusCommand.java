package org.eu.hanana.reimu.app.mod.enchantment.core.internal;

import com.google.gson.internal.JavaVersion;
import com.sun.management.OperatingSystemMXBean;
import com.sun.tools.attach.VirtualMachine;
import lombok.SneakyThrows;
import org.eu.hanana.reimu.app.mod.enchantment.core.api.AbstractCommandListener;
import org.eu.hanana.reimu.app.mod.enchantment.core.api.ICommandListener;
import org.eu.hanana.reimu.app.mod.enchantment.event.CommandEvent;
import org.eu.hanana.reimu.lib.satori.v1.protocol.Login;
import sun.misc.Unsafe;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;

public class StatusCommand extends AbstractCommandListener {
    public StatusCommand() {
        super("status");
    }

    @SneakyThrows
    @Override
    public void process(CommandEvent event) {
        var sb = new StringBuilder("[HANANA ENCHANTMENT] 系统状态\n");
        enchantmentManager.logins.forEach((s, logins) -> {
            sb.append("[").append(s).append("]:\n").append("{\n");
            for (Login login : logins) {
                sb.append("  ").append(login.platform).append(":").append(login.status).append("\n");
            }
            sb.append("}\n");
        });
        sb.append("======\n");
        Runtime runtime = Runtime.getRuntime();

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = allocatedMemory - freeMemory;

        sb.append(String.format("最大内存 (Max Memory): %.2f MB\n",maxMemory / 1048576.0));
        sb.append(String.format("已分配内存 (Allocated Memory): %.2f MB\n", allocatedMemory / 1048576.0));
        sb.append(String.format("已使用内存 (Used Memory): %.2f MB\n", usedMemory / 1048576.0));
        sb.append(String.format("空闲内存 (Free Memory): %.2f MB\n", freeMemory / 1048576.0));
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 获取进程总虚拟内存（包括 Heap 和 Off-Heap）
        long virtualMemory = osBean.getCommittedVirtualMemorySize();

        // 获取物理内存占用
        long totalPhysicalMemory = osBean.getTotalPhysicalMemorySize();
        long freePhysicalMemory = osBean.getFreePhysicalMemorySize();
        long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;

        sb.append(String.format("虚拟内存 (Virtual Memory): %.2f MB\n", virtualMemory / 1048576.0));
        sb.append(String.format("总物理内存 (Total Physical Memory): %.2f MB\n", totalPhysicalMemory / 1048576.0));
        sb.append(String.format("已使用物理内存 (Used Physical Memory): %.2f MB\n", usedPhysicalMemory / 1048576.0));
        sb.append(String.format("空闲物理内存 (Free Physical Memory): %.2f MB\n", freePhysicalMemory / 1048576.0));
        // 获取系统和进程的 CPU 使用率，返回值范围为 0.0 到 1.0
        double systemCpuLoad = osBean.getCpuLoad() * 100;
        double processCpuLoad = osBean.getProcessCpuLoad() * 100;
        sb.append(String.format("系统 CPU 使用率: %.2f%%\n", systemCpuLoad));
        sb.append(String.format("进程 CPU 使用率: %.2f%%\n", processCpuLoad));
        sb.append("===== JVM 信息 =====\n");
        // JVM 相关信息
        String javaVersion = System.getProperty("java.version");
        String javaVendor = System.getProperty("java.vendor");
        String javaHome = System.getProperty("java.home");
        sb.append(String.format("Java 版本: %s\n", javaVersion));
        sb.append(String.format("Java 供应商: %s\n", javaVendor));
        sb.append(String.format("Java 安装路径: %s\n", javaHome));
        enchantmentManager.sendMessage(sb.toString(),event.messageEvent.getChannel().id,event.messageEvent.login);
    }

    @Override
    public String getDescribe() {
        return "系统状态";
    }
}
