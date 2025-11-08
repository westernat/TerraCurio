package org.confluence.terra_curio.common.attachment;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.primitive.PrimitiveValue;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.init.TCAttachments;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class AccessoriesValueCommand {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> INFOS = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, TerraCurio.MODID);

    public static final Supplier<ArgumentTypeInfo<?, ?>> VALUE_TYPE = INFOS.register("value_type", () -> ArgumentTypeInfos.registerByClass(ValueTypeArgument.class, SingletonArgumentInfo.contextFree(ValueTypeArgument::type)));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("terra_curio").requires(sourceStack -> sourceStack.hasPermission(2))
                .then(Commands.argument("type", ValueTypeArgument.type()).executes(context -> {
                    String type = context.getArgument("type", String.class);
                    ValueType<?, ? extends PrimitiveValue<?>> valueType = ValueType.TYPES.get(TerraCurio.asResource(type));
                    CommandSourceStack source = context.getSource();
                    if (valueType == null) {
                        source.sendFailure(Component.translatable("argument.terra_curio.unknown_type", type));
                        return 0;
                    } else {
                        AccessoriesAttachment attachment = source.getEntityOrException().getData(TCAttachments.ACCESSORIES);
                        if (valueType.defaultValue() == Unit.INSTANCE) {
                            source.sendSystemMessage(Component.literal("Contains: " + attachment.contains(valueType)));
                        } else {
                            for (String description : attachment.getDescription(valueType)) {
                                source.sendSystemMessage(Component.literal(description));
                            }
                        }
                        return 1;
                    }
                }))
        );
    }

    public static class ValueTypeArgument implements ArgumentType<String> {
        private static final DynamicCommandExceptionType UNKNOWN_TYPE = new DynamicCommandExceptionType(type -> Component.translatable("argument.terra_curio.unknown_type", type));
        private static final List<String> EXAMPLES = Lists.newArrayList("auto_attack");
        private static final Function<ValueType<?, ? extends PrimitiveValue<?>>, String> MAPPER = type -> type.key().toString();
        private static Set<String> AVAILABLE;

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            try {
                String id = reader.readUnquotedString();
                if (ValueType.TYPES.containsKey(TerraCurio.asResource(id))) {
                    return id;
                } else {
                    throw UNKNOWN_TYPE.create(id);
                }
            } catch (Exception e) {
                throw UNKNOWN_TYPE.create(e);
            }
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            if (AVAILABLE == null) {
                AVAILABLE = new LinkedHashSet<>(AccessoriesAttachment.UNITS_REQUIRE_UPDATE.stream().map(MAPPER).toList());
                AVAILABLE.addAll(AccessoriesAttachment.OTHER_REQUIRE_UPDATE.stream().map(MAPPER).toList());
            }
            return SharedSuggestionProvider.suggest(AVAILABLE, builder);
        }

        @Override
        public Collection<String> getExamples() {
            return EXAMPLES;
        }

        public static ValueTypeArgument type() {
            return new ValueTypeArgument();
        }
    }
}
