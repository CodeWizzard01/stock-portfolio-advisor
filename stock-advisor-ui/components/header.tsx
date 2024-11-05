"use client";

import { ModeToggle } from "./ui/mode-toggle";
import Typography from "./ui/typography";
import React from "react";



function Header() {
  return (
    <header className="border-b py-3 flex flex-row justify-between items-center">
      <div className="left-icon flex items-center space-x-4">

          <Typography
            element="h4"
            as="h4"
            className="font-poppins text-2xl font-semibold text-gray-300"
          >
            Stock Portfolio Advisor
          </Typography>
      </div>
      <div></div>
      <div className="right-icons flex items-center space-x-4">
        <ModeToggle />
      </div>
    </header>
  );
}

export default Header;
