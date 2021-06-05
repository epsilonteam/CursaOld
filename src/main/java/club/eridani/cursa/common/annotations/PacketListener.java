package club.eridani.cursa.common.annotations;

import club.eridani.cursa.common.types.IO;
import club.eridani.cursa.common.types.Packets;
import net.minecraft.network.Packet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PacketListener {
    IO channel();
    Class<? extends Packet<?>> target() default Packets.class;
}
