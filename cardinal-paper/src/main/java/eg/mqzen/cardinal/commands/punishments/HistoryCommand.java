package eg.mqzen.cardinal.commands.punishments;


import eg.mqzen.cardinal.Cardinal;
import eg.mqzen.cardinal.api.punishments.Punishment;
import eg.mqzen.cardinal.punishments.gui.HistoryPage;
import eg.mqzen.cardinal.punishments.gui.PunishmentPageComponent;
import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.Pagination;
import io.github.mqzen.menus.base.pagination.exception.InvalidPageException;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.Command;
import studio.mevera.imperat.annotations.Usage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Command("history")
public class HistoryCommand {


    @Usage
    public void def(Player source) {

        Cardinal.getInstance().getPunishmentManager()
                .getHistoryService()
                .getRecentPunishments(Duration.ofDays(365), 100)
                .onSuccess((punishmentsQueue)-> {

                    Pagination pagination = Pagination.auto(Cardinal.getInstance().getLotus())
                            .creator(new HistoryPage())
                            .componentProvider(()-> {
                                List<PageComponent> components = new ArrayList<>();
                                for (Punishment<?> punishment : punishmentsQueue) {
                                    components.add(new PunishmentPageComponent(punishment));
                                }
                                return components;
                            })
                            .build();

                    try {
                        pagination.open(source);
                    } catch (InvalidPageException ex) {
                        ex.printStackTrace();
                        //Pagination is empty or something else happened
                        source.sendMessage("There is no components or pages to display !!");
                    }

                });
    }


}
