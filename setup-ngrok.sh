#!/bin/bash

# ngrok Agent CLI Quickstart - Setup Automatizado
# Este script configura tudo que você precisa para o ngrok quickstart

set -e

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         ngrok Agent CLI Quickstart - Setup Automatizado        ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

NGROK_TOKEN="3IU7zmxRvf4yw7MfrsweBCGKRAS_2ZYJoG9MBexS9AK2Ga2Fb"
NGROK_DOMAIN="mustiness-bootleg-subzero.ngrok-free.dev"

# Função para colorir outputs
print_step() {
    echo ""
    echo "📍 PASSO: $1"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
}

print_success() {
    echo "✅ $1"
}

print_error() {
    echo "❌ $1"
    exit 1
}

# Passo 1: Verificar se ngrok está instalado
print_step "Verificando instalação do ngrok"

if ! command -v ngrok &> /dev/null; then
    print_error "ngrok não está instalado!"
    echo ""
    echo "📥 Instale com:"
    echo "   macOS:   brew install ngrok/ngrok/ngrok"
    echo "   Linux:   sudo apt-get install ngrok"
    echo "   Windows: choco install ngrok"
fi

print_success "ngrok encontrado: $(ngrok version)"

# Passo 2: Configurar autenticação
print_step "Configurando autenticação ngrok"

ngrok config add-authtoken "$NGROK_TOKEN" 2>/dev/null || true
print_success "Token de autenticação configurado"

# Passo 3: Verificar configuração
print_step "Verificando configuração"

if ngrok config check > /dev/null 2>&1; then
    print_success "Configuração válida"
else
    print_error "Problema na configuração do ngrok"
fi

# Passo 4: Criar arquivos necessários
print_step "Preparando arquivos"

if [ ! -f "ngrok-quickstart-app.py" ]; then
    print_error "arquivo ngrok-quickstart-app.py não encontrado"
else
    print_success "ngrok-quickstart-app.py encontrado"
fi

if [ ! -f "ngrok.yml" ]; then
    print_error "arquivo ngrok.yml não encontrado"
else
    print_success "ngrok.yml encontrado"
fi

# Passo 5: Informações finais
print_step "Setup Completo!"

echo ""
echo "🎉 Tudo pronto! Próximas etapas:"
echo ""
echo "1️⃣  Abra Terminal 1 e inicie o servidor:"
echo "   python3 ngrok-quickstart-app.py"
echo ""
echo "2️⃣  Abra Terminal 2 e exponha com ngrok:"
echo "   ngrok http 8080"
echo ""
echo "3️⃣  Seu app estará em:"
echo "   https://$NGROK_DOMAIN"
echo ""
echo "4️⃣  Para usar Traffic Policy com OAuth:"
echo "   - Edite ngrok.yml com suas credenciais Google"
echo "   - Execute: ngrok start cli-quickstart"
echo ""
echo "📖 Guia completo em: NGROK_QUICKSTART_GUIDE.md"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
