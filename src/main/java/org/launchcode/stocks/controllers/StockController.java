package org.launchcode.stocks.controllers;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.stocks.models.Stock;
import org.launchcode.stocks.models.StockHolding;
import org.launchcode.stocks.models.StockLookupException;
import org.launchcode.stocks.models.User;
import org.launchcode.stocks.models.dao.StockHoldingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Chris Bay on 5/17/15.
 */
@Controller
public class StockController extends AbstractController {

    @Autowired
    StockHoldingDao stockHoldingDao;

    @RequestMapping(value = "/quote", method = RequestMethod.GET)
    public String quoteForm(Model model) {

        // pass data to template
        model.addAttribute("title", "Quote");
        model.addAttribute("quoteNavClass", "active");
        return "quote_form";
    }

    @RequestMapping(value = "/quote", method = RequestMethod.POST)
    public String quote(String symbol, Model model) {
    	
    	Stock target = null;

        // TODO - Implement quote lookup
    	try {
			target = Stock.lookupStock(symbol);
		} catch (StockLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String stock_desc = target.toString();
    	Float stock_price = target.getPrice();

        // pass data to template
    	model.addAttribute("stock_desc", stock_desc);
    	model.addAttribute("stock_price", stock_price);
        model.addAttribute("title", "Quote");
        model.addAttribute("quoteNavClass", "active");

        return "quote_display";
    }

    @RequestMapping(value = "/buy", method = RequestMethod.GET)
    public String buyForm(Model model) {

        model.addAttribute("title", "Buy");
        model.addAttribute("action", "/buy");
        model.addAttribute("buyNavClass", "active");
        return "transaction_form";
    }

    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    public String buy(String symbol, int numberOfShares, HttpServletRequest request, Model model) {

        // TODO - Implement buy action
    	User holder = getUserFromSession(request);
    	StockHolding purchase = null;
    	try {
			purchase = StockHolding.buyShares(holder, symbol, numberOfShares);
		} catch (StockLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	stockHoldingDao.save(purchase);
    	String company = "";
		try {
			company = Stock.lookupStock(purchase.getSymbol()).toString();
		} catch (StockLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String confirmMessage = "Purchased: " + purchase.getSharesOwned() + " shares of " + company + "(" + purchase.getSymbol() +")";

        model.addAttribute("title", "Buy");
        model.addAttribute("action", "/buy");
        model.addAttribute("buyNavClass", "active");
        model.addAttribute("confirmMessage", confirmMessage);

        return "transaction_confirm";
    }

    @RequestMapping(value = "/sell", method = RequestMethod.GET)
    public String sellForm(Model model) {
        model.addAttribute("title", "Sell");
        model.addAttribute("action", "/sell");
        model.addAttribute("sellNavClass", "active");
        return "transaction_form";
    }

    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public String sell(String symbol, int numberOfShares, HttpServletRequest request, Model model) {

        // TODO - Implement sell action

        model.addAttribute("title", "Sell");
        model.addAttribute("action", "/sell");
        model.addAttribute("sellNavClass", "active");

        return "transaction_confirm";
    }

}
