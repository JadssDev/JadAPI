package dev.jadss.jadapi.management.nms.objects.registry;

import java.util.function.Function;

@FunctionalInterface
public interface RegistryGetter extends Function<Void, RegistryAdapter> {

}