package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
	public Result getProduct(String productId) {
		return ok(views.html.index.render());
	}
}
