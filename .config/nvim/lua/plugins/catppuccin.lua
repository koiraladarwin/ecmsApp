return {
  {
    "bluz71/vim-moonfly-colors",
    lazy = false,
    name = "moonfly",
    priority = 1000,
    config = function()
      vim.cmd.colorscheme "moonfly"
      vim.o.background = "dark" -- Ensure the background is set to dark
    end,
  }
}
--
--
-- return {
--   {
--     "rebelot/kanagawa.nvim",
--     lazy = false,
--     priority = 1000,
--
--     config = function()
--       require("kanagawa").setup({
--         transparent = true,
--       })
--       vim.cmd.colorscheme "kanagawa"
--     end
--   }
-- }
-- return {
--   {
--     "catppuccin/nvim",
--     lazy = false,
--     name = "catppuccin",
--     priority = 1000,
--
--     config = function()
--       require("catppuccin").setup({
--         transparent_background = true,
--       })
--       vim.cmd.colorscheme "catppuccin-mocha"
--     end
--   }
-- }

-- return {
--   {
--     "sainnhe/sonokai",
--     lazy = false,
--     name = "sonokai",
--     priority = 1000,
--     config = function()
--       vim.g.sonokai_enable_italic = 1
--       vim.g.sonokai_transparent_background = 1
--       vim.g.sonokai_style = "andromeda"
--       vim.cmd.colorscheme "sonokai"
--
--       -- Set transparency for the line numbers (left side in your case)
--       vim.cmd([[
--               highlight LineNr guibg=NONE ctermbg=NONE
--               highlight CursorLineNr guibg=NONE ctermbg=NONE
--               highlight SignColumn guibg=NONE ctermbg=NONE
--               highlight GitSignsAdd guibg=NONE ctermbg=NONE
--               highlight GitSignsChange guibg=NONE ctermbg=NONE
--               highlight GitSignsDelete guibg=NONE ctermbg=NONE
--             ]])
--
--       -- Set transparency for the rest of the UI (optional)
--       vim.cmd([[
--               highlight Normal guibg=NONE ctermbg=NONE
--               highlight NonText guibg=NONE ctermbg=NONE
--               highlight NormalNC guibg=NONE ctermbg=NONE
--               highlight StatusLine guibg=NONE ctermbg=NONE
--               highlight StatusLineNC guibg=NONE ctermbg=NONE
--               highlight TabLine guibg=NONE ctermbg=NONE
--               highlight TabLineFill guibg=NONE ctermbg=NONE
--               highlight TabLineSel guibg=NONE ctermbg=NONE
--             ]])
--
--       -- Set transparency for Telescope and NERDTree (if used)
--       vim.cmd([[
--               highlight TelescopeNormal guibg=NONE ctermbg=NONE
--               highlight NvimTreeNormal guibg=NONE ctermbg=NONE
--             ]])
--     end
--   }
-- }

-- return {
--   {
--     "folke/tokyonight.nvim",
--     lazy = false,
--     name = "tokyonight",
--     priority = 1000,
--
--     config = function()
--       require("tokyonight").setup({
--         style = "storm",  -- Choose between "storm" (darker) or "night" (calmer)
--         transparent = true,  -- Enable transparent background
--       })
--       vim.cmd.colorscheme "tokyonight"  -- Apply the theme
--     end
--   }
-- }

-- return {
--   {
--     "oxfist/night-owl.nvim",
--     lazy = false,
--     name = "night-owl",
--     priority = 1000,
--
--     config = function()
--       -- Set the colorscheme to night-owl
--       vim.cmd.colorscheme("night-owl")
--
--       -- Set the background to dark, which is usually required for the theme
--       vim.api.nvim_set_option("background", "dark")
--
--       -- Set transparency for the line numbers (left side in your case)
--       vim.cmd([[
--         highlight LineNr guibg=NONE ctermbg=NONE
--         highlight CursorLineNr guibg=NONE ctermbg=NONE
--         highlight SignColumn guibg=NONE ctermbg=NONE
--         highlight GitSignsAdd guibg=NONE ctermbg=NONE
--         highlight GitSignsChange guibg=NONE ctermbg=NONE
--         highlight GitSignsDelete guibg=NONE ctermbg=NONE
--       ]])
--
--       -- Set transparency for the rest of the UI (optional)
--       vim.cmd([[
--         highlight Normal guibg=NONE ctermbg=NONE
--         highlight NonText guibg=NONE ctermbg=NONE
--         highlight NormalNC guibg=NONE ctermbg=NONE
--         highlight StatusLine guibg=NONE ctermbg=NONE
--         highlight StatusLineNC guibg=NONE ctermbg=NONE
--         highlight TabLine guibg=NONE ctermbg=NONE
--         highlight TabLineFill guibg=NONE ctermbg=NONE
--         highlight TabLineSel guibg=NONE ctermbg=NONE
--       ]])
--
--       -- Set transparency for Telescope and NERDTree (if used)
--       vim.cmd([[
--         highlight TelescopeNormal guibg=NONE ctermbg=NONE
--         highlight NvimTreeNormal guibg=NONE ctermbg=NONE
--       ]])
--     end
--   }
-- }
