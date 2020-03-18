package kr.minecheat.mcauth.packetIO.impls;

import kr.minecheat.mcauth.mcdata.*;
import kr.minecheat.mcauth.packetIO.PacketDataIO;
import kr.minecheat.mcauth.packetIO.PacketDataIOProvider;
import kr.minecheat.mcauth.packetIO.PacketDataReader;
import kr.minecheat.mcauth.packetIO.PacketDataWriter;

import java.util.HashMap;
import java.util.UUID;

public class PacketDataIOProviderImpl extends PacketDataIOProvider {
    private static HashMap<Class, PacketDataReader> readers;
    private static HashMap<Class, PacketDataWriter> writers;

    public PacketDataIOProviderImpl() {
        readers = new HashMap<>();
        writers = new HashMap<>();

        register(Boolean.class, new PacketBooleanIO());
        register(Byte.class, new PacketByteIO());
        register(Float.class, new PacketFloatIO());
        register(Integer.class, new PacketIntegerIO());
        register(Long.class, new PacketLongIO());
        register(Short.class, new PacketShortIO());
        register(UnsignedByte.class, new PacketUnsignedByteIO());
        register(UnsignedShort.class, new PacketUnsignedShortIO());
        register(VarInt.class, new PacketVarIntIO());
        register(VarLong.class, new PacketVarLongIO());
        register(Location.class, new PacketLocationIO());
        register(String.class, new PacketStringIO());
        register(UUID.class, new PacketUUIDIO());
    }

    private void register(Class c, PacketDataIO io) {
        readers.put(c, io);
        writers.put(c, io);
    }

    @Override
    public <T> PacketDataReader<T> getDataReader(Class<T> t) {
        return (PacketDataReader<T>) readers.get(t);
    }

    @Override
    public <T> PacketDataWriter<T> getDataWriter(Class<T> t) {
        return (PacketDataWriter<T>) writers.get(t);
    }
}
