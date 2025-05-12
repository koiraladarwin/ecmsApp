return {
  {
    "xiyaowong/nvim-cursorword",
    config = function()
      -- Load the colorscheme
   
      -- Customize the CursorWord highlight with a white background
vim.cmd [[
        highlight CursorWord cterm=bold,italic gui=bold,italic guifg=#FFCC00 guibg=NONE
      ]]    end
  }
}
